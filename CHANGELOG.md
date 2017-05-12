# Change Log

## 0.3.0

- Upgrade to work with Farmhand 0.9.0
- New page to show scheduled jobs
- Removed several barriers to using this as a library. Previously this project
  was designed to be used as a standalone process. But now the recommended
  procedure will be to embed it.

## 0.2.2

- Fix several issues with using the UI as a dependency to another project.

## 0.2.1

- Upgraded Farmhand dependency to 0.8.0

## 0.2.0

- New `/queues` route to show list of queues, their sizes, and give a button to
  purge each queue
- `/` route now redirects to `/queues` instead of `/failed`
- Upgraded bootstrap to 4.0.0-alpha.6
- Restyled to use darker navbar
- Reorganized templates directory
- Prev/Next links in registry pages are now prettier (uses a Bootstrap nav),
  the code is DRYed up using Selmer imports, and the links now also appear at
  the bottom of the pages.

## 0.1.2

- Truncate job IDs in each of the pages. This saves some screen real estate.
  The full ID can be viewed either by hovering over the ID or by going to the
  details page for the job.
- Nrepl bind address default is now `127.0.0.1` instead of `localhost`.
- Links to view job details have all been fixed
- Button to requeue job has been fixed

## 0.1.1 - 2017-02-11

- First release
