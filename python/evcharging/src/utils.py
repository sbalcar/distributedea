import numpy
import math

import settings


def minimum_charging_speed(remaining_steps, active_request):
    charge_15min = active_request.max_charging_speed / 4
    return max(0.0, active_request.remaining_charge - (remaining_steps - 1)*charge_15min)*4

def constant_charging_speed(remaining_steps, active_request):
    avg_charge_15min = active_request.remaining_charge/remaining_steps
    return avg_charge_15min*4

def relu(x):
    return numpy.maximum(x, 0)

def sigmoid(x):
    return 1 / (1 + numpy.exp(-numpy.clip(x, -15, 15)))

def remaining_steps(ar, current_time):
    return (ar.available_until - current_time)//settings.TIME_STEP

def encode_time(current_time):
    seconds = current_time.hour*3600+current_time.minute*60
    day_fraction = seconds/(24*3600)
    return math.cos(2*math.pi*day_fraction), math.sin(2*math.pi*day_fraction)