#!/bin/sh -e

lein run with-profile production -m qcast.cache/init
