# snoozeclient (testing)

The Snooze command-line interface (CLI).
What's new ?

* Get the local controller lists.

        $) snoozeclient hosts

* Start on a specific local controller.

        $) snoozeclient add -vcn <cluster> -vmt <vmtemplate> -hid <hostuuid>

* Resize vm 

        $) snoozeclient resize -vcn <cluster> [-vmn <vmname>]
 

## Installation and Usage

Please refer to <http://snooze.inria.fr/documentation/administration/user-manual/> for the installation and usage documentation.

## Development

* Fork the repository
* Make your bug fixes or feature additions by following our coding conventions (see the [snoozecheckstyle](https://github.com/snoozesoftware/snoozecheckstyle) repository)
* Send a pull request

## Copyright

Snooze is copyrighted by [INRIA](http://www.inria.fr/en) and released under the GPL v2 license (see LICENSE.txt). It is registered at the [APP (Agence de Protection des Programmes)](http://www.app.asso.fr/) under the number IDDN.FR.001.100033.000.S.P.2012.000.10000.
