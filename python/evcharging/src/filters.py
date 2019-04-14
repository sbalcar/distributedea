class LowPassFilter:

    def __init__(self, planner, alpha):
        self.planner = planner
        self.alpha = alpha
        self.last_charge = 0
        self.updates_since_last_charge = 0

    def get_charge(self, remaining_steps, ar, **kwargs):
        if self.updates_since_last_charge > 1:
            self.last_charge = 0
        self.updates_since_last_charge = 0
        raw_charge = self.planner.get_charge(remaining_steps, ar, **kwargs)
        charge = self.alpha*self.last_charge + (1-self.alpha)*raw_charge
        self.last_charge = charge
        return charge

    def copy(self):
        copy_planner = self.planner.copy()
        return LowPassFilter(copy_planner, self.alpha)

    def update_info(self, *args, **kwargs):
        self.updates_since_last_charge += 1
        self.planner.update_info(*args, **kwargs)