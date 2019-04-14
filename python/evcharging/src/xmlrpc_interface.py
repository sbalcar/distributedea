__author__ = 'Martin'

from xmlrpc.server import SimpleXMLRPCServer
import json
import os
import sys
import time

import fitness
import planners
import utils

stop_server = False

class DagEvalServer:

    def __init__(self, config_file):
        with open(config_file, 'r') as cf:
            self.config = json.load(cf)
            self.planner = eval(self.config['planner'])

    def evaluate(self, json_string):
        ind = json.loads(json_string)
        return float(fitness.evaluate_fitness(ind, self.config)[0]),

    def get_individual_size(self):
        return self.planner.vectorized_size(),

    def quit(self):
        global stop_server
        stop_server = True
        return json.dumps('OK')


if __name__ == '__main__':

    config_file = sys.argv[1]
    port_number = int(sys.argv[2])
    eval_server = DagEvalServer(config_file)

    server = SimpleXMLRPCServer(('localhost', port_number))
    server.register_instance(eval_server)

    while not stop_server:
        server.handle_request()
