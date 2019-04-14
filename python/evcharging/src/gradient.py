import argparse
import datetime
import functools
import json
import os
import pickle

import numpy

import evolution
import planners
import simulator
import utils

sim = simulator.Simulator(requests_file='../data/requests_1.csv',
                          data_file='../data/15min-car-austin-jan-mar-2015.zip',
                          start_time=datetime.datetime(2015, 1, 3, 16, 0), planner=planners.RandomPlanner())


def grad_helper(x):
    return (x[2](x[0], start=x[4], steps=4 * 24 + 2 * 4 * 24) - x[3]) / x[1]

def gradient(f, x, fx=None, start=None):
    fx = f(x, start=start, steps=4*24+2*4*24)
    xphs = []
    for i in range(len(x)):
        xph = numpy.copy(x)
        xph[i] = xph[i] + 0.000000001
        d = xph[i] - x[i]
        xphs.append((xph, d, f, fx, start))

    grad = map(grad_helper, xphs)
    return numpy.array(grad)

def func_2(x, config, additional_planner_params, start=None, steps=None):
    return evolution.evaluate_fitness(x, config = config, additional_planner_params = additional_planner_params,
                                      start=start, steps=steps)[0]

