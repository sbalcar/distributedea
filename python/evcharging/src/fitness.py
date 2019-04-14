import argparse
import datetime
import json
import numpy
import os
import pickle

import simulator
import planners
import utils

sim = simulator.Simulator(requests_file='../data/requests_1.csv',
                          data_file='../data/15min-car-austin-jan-mar-2015.zip',
                          start_time=datetime.datetime(2015, 1, 3, 16, 0), planner=planners.RandomPlanner())

def evaluate_fitness(ind, config, additional_planner_params=None, start=None, steps=None):
    print('.', end='', flush=True)
    planner = eval(config['planner'])
    planner.set_network(ind)
    if additional_planner_params:
        planner.set_additional_params(additional_planner_params)

    if not start:
        start=datetime.datetime(2015, 1, 2, 16)

    if not steps:
        steps = 4*168*2+4*24

    sim.reset(start_time=start, planner=planner)
    for i in range(steps):
        sim.step()

    fitness = numpy.std([(tc + oc) for (_, tc, oc) in sim.simulation_log[4 * 24:]])  # ignore first day to pre-warm the model

    return fitness,