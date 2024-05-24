Tempera access point
====================

[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)

## Documentation

As is customary for python projects, [read the docs](https://about.readthedocs.com/?ref=readthedocs.com)
style documentation is provided for this package.

To build and visualize the documentation pages

```bash
# Install the tempera access point python package "." i.e., the code in this directory
# with the optional docs dependencies "[docs]".
$ pip install .[docs]
# Note: if you don't have python 3.11 installed locally, you can install sphinx separately
# but you'll only be able to access the documentation and not run the code locally.
$ pip install sphinx sphinx-rtd-theme

# Go to the docs directory, and build it with the makefile provided by sphinx.
$ cd ./docs
$ make html
# The output is found in ./docs/build/html, just go to that directory and open the index.html
# file with a browser or IDE of your choice.
```
