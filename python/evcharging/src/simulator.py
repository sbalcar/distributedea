import pandas as pd
import numpy as np
import datetime
import planners
import heapq

import utils
import settings

np.seterr(all='raise')

class ActiveRequest:

    def __init__(self, available_after, available_until, remaining_charge, max_charging_speed, houseid, initial_steps, initial_charge):
        self.available_until = available_until
        self.remaining_charge = remaining_charge
        self.max_charging_speed = max_charging_speed
        self.houseid = houseid
        self.available_after = available_after
        self.initial_steps = initial_steps
        self.initial_charge = initial_charge

class Simulator:

    def __init__(self, requests_file, data_file, start_time, planner):
        self.requests_frame = pd.read_csv(requests_file, parse_dates=[4,5]).sort_values(by='available_after')
        self.electricity_data = pd.read_csv(data_file, index_col=['local_15min', 'dataid'], delimiter=';', parse_dates=[1])
        self.electricity_data.fillna(0, inplace=True)
        self.electricity_use = self.electricity_data['use'] - self.electricity_data['car1']
        self.electricity_use = self.electricity_use.unstack(0)
        self.electricity_use.fillna(0, inplace=True)
        self.overall_consumption = self.electricity_use.sum(axis=0)
        self.requests = []

        self.reset(start_time, planner)

    def reset(self, start_time, planner):
        self.current_time = start_time
        self.requests = []
        self.active_requests = []
        heapq.heapify(self.active_requests)
        self.active_households = set([])
        self.missed_charge = 0
        self.last_total_charge = 0
        self.planner = planner

        self.planners = {house_id: self.planner.copy() for house_id in np.unique(self.requests_frame.house_id)}

        for req in self.requests_frame.itertuples():

            r = ActiveRequest(available_after=req.available_after,
                              available_until=req.available_before,
                              remaining_charge=req.required_charge,
                              max_charging_speed=req.max_charging_speed,
                              houseid=req.house_id,
                              initial_steps=(req.available_before - self.current_time) // settings.TIME_STEP,
                              initial_charge=req.required_charge)

            if not np.isnan(r.remaining_charge) and r.remaining_charge > 0.01:
                self.requests.append(r)

        self.requests = list(sorted(self.requests, key=lambda r: r.available_after))

        self.req_idx = 0
        while self.requests[self.req_idx].available_after <= start_time - settings.TIME_STEP:
            self.req_idx += 1

        self.simulation_log = []

    def step(self):

        if datetime.datetime(2015, 3, 8, 1, 45) < self.current_time < datetime.datetime(2015, 3, 8, 3, 0):
            self.current_time += settings.TIME_STEP
            return # daylight saving time change

        end = self.current_time + settings.TIME_STEP

        # remove cars that left in the last 15 mins
        while self.active_requests and self.active_requests[0][0] < end:
            self.missed_charge += self.active_requests[0][2].remaining_charge # compute missed charge
            _, r_idx, r = heapq.heappop(self.active_requests)
            if r.houseid in self.active_households:
                self.active_households.remove(r.houseid)
            else:
                print(r.houseid, r_idx, r.available_after, r.available_until, r.remaining_charge, r.initial_charge)
                raise(KeyError(r.houseid))

        while self.req_idx < len(self.requests) and self.requests[self.req_idx].available_after <= self.current_time:
            r = self.requests[self.req_idx]
            if r.houseid not in self.active_households:
                # in rare cases there are overlapping requests, skip them, probably an artifact of the way
                # how requests are generated

                heapq.heappush(self.active_requests, (r.available_until, self.req_idx, r))
                self.active_households.add(r.houseid)

            self.req_idx += 1

        # charge cars
        total_charge = 0
        overall_consumption = self.overall_consumption.at[self.current_time]

        for house_id, planner in self.planners.items():
            # give each planner info on last consumption of household and last overall consumption
            if house_id not in self.active_households: # update household without active requests, other will be updated later
                planner.update_info(self.electricity_use.at[house_id, self.current_time],
                                    overall_consumption + self.last_total_charge, current_time=self.current_time)

        for _, _, ar in self.active_requests:
            self.planners[ar.houseid].update_info(self.electricity_use.at[ar.houseid, self.current_time],
                                                  overall_consumption + self.last_total_charge,
                                                  ar=ar, current_time=self.current_time)
            if ar.remaining_charge < 0.0001:
                continue  # already charged, skip request
            remaining_steps = utils.remaining_steps(ar, self.current_time)
            min_charging_speed = utils.minimum_charging_speed(remaining_steps, ar)
            raw_charge = self.planners[ar.houseid].get_charge(remaining_steps, ar, current_time=self.current_time)
            charge = min(ar.max_charging_speed, raw_charge) # ensure the charging is not faster than max
            charge = max(min_charging_speed, charge) # make sure the charge is able to charge the vehicle
            charge = min(charge, ar.remaining_charge*4) # make sure we do not over-charge the vehicle
            ar.remaining_charge -= charge/4
            total_charge += charge

        self.last_total_charge = total_charge

        self.simulation_log.append((self.current_time, total_charge, overall_consumption))

        self.current_time += settings.TIME_STEP

    # def get_gradient_data(self):
    #    return [p.last_states for _, p in self.planners.items()], self.simulation_log

if __name__ == '__main__':
    sim = Simulator(requests_file='../data/requests_5.csv', data_file='../data/15min-car-austin-jan-mar-2015.zip',
                    start_time=datetime.datetime(2015, 1, 1, 0, 0), planner=planners.MaxPossibleCharge())

    while sim.current_time < datetime.datetime(2015, 4, 1, 0, 0):
        sim.step()

    max_consumption = max(tc + oc for (_, tc, oc) in sim.simulation_log)
    min_consumption = min(tc + oc for (_, tc, oc) in sim.simulation_log)
    print(sim.simulation_log)
    print("max_cons:", max_consumption)
    print("min_cons:", min_consumption)
    print([tc + oc for (_, tc, oc) in sim.simulation_log])
    print("missed charge", sim.missed_charge)
    print("end time:", sim.current_time)
    print("objective:", max_consumption - min_consumption)