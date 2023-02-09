# meshanka.frontend

Frontend part of website for Meshanka - zonal Slavic auxiliary language.

## Overview

TBD

## Development

To get an interactive development environment run:

    lein fig:build

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

	lein clean

To create a production build run:

	lein clean
	lein fig:min

## Testing

Same as for main development you just run this same command

    lein fig:build

Then go to http://localhost:9500/figwheel-extra-main/auto-testing
There should be UI with hot-reloading test UI.
All other ways that is in the figwheel docs or in the temlate docs don't make tests hotreload.

## License

Copyright © 2023 Arseny Mikhalev

Distributed under the Eclipse Public License either version 1.0 or any later version.
