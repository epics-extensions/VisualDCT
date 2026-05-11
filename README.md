# VisualDCT (aka VDCT) ![Build Status](https://github.com/epics-extensions/VisualDCT/actions/workflows/build.yml/badge.svg)

VisualDCT is the Visual Database Configuration Tool for
[EPICS](https://epics-controls.org/) Databases.

It was developed by [Cosylab](http://www.cosylab.com/).
This development was funded by SLS, APS, Diamond and SNS. 

## Downloads

Distribution archives of released versions (containing binaries,
sources, javadoc, and the documentation web site) are available from the
[releases page](https://github.com/epics-extensions/VisualDCT/releases).

## Web Site

VisualDCT has a generated static web site that contains detailed information,
including user manuals, release notes, reference and project documentation.

The current (development) version of the generated web site is 
[available on-line](https://epics-extensions.github.io/VisualDCT/), the complete
website is included in the distribution archives (in the `site` folder).

## Building from Sources

VisualDCT is a [Maven](https://maven.apache.org/) project.
Any recent Java IDE should be able to open and compile it.
VisualDCT requires Java 8 and is known to compile using Java 8-21.

Apart from the default lifecycle, two additional Maven goals are useful:

  * **site** Generate the website with user, reference and project documentation
  * **assembly:single** Generate the distribution archives as tar and zip

## Continuous Integration

Continuous Integration and deployment is provided through 
[GitHub Actions](https://github.com/epics-extensions/VisualDCT/actions).

## Bugs

Any bugs should be reported as
[issues](https://github.com/epics-extensions/VisualDCT/issues)
in the GitHub project.

## Contributing

Pull requests are always welcome.

For non-trivial changes and additions, please first contact the authors
and/or use the EPICS
[Tech-Talk](https://epics-controls.org/resources-and-support/mailing-lists/) mail exploder
to discuss and review your plans.
