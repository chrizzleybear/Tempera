Tempera access point
====================

[![Code style: black](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)

## Documentation

As is customary for python projects, [read the docs](https://about.readthedocs.com/?ref=readthedocs.com)
style documentation is provided for this package.

To build and visualize the documentation pages you have to
have a python version >= 3.11 on your system (i.e., locally).

> :mega: Alternatively, in the GitLab project repository, you can also
> go to build -> artefacts and look for the most recent entry
> named *pages*. Download this pages artifact and unzip it.
> Then you can just open the .../docs/index.html file in your
> browser (e.g., `$ firefox /path/to/index.html`) and view the
> complete documentation as if you built it yourself with sphinx.

```bash
# Install the tempera access point python package "." i.e., the code in this directory
# with the optional docs dependencies "[docs]".
$ pip install .[docs]

# Go to the docs directory, and build it with the makefile provided by sphinx.
$ cd ./docs
$ make html
# The output is found in ./docs/build/html, just go to that directory and open the index.html
# file with a browser or IDE of your choice.
```
