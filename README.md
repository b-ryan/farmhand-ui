# Farmhand UI

The UI companion to [Farmhand](https://github.com/b-ryan/farmhand). Farmhand UI
is a web interface that shows you lists of pending, scheduled, completed, and
failed jobs. You can use it to get details of a job, including when it was
queued, the results of the job, or why it failed. In addition, failed jobs can
be re-queued right from the UI.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Installation](#installation)
- [Usage](#usage)
  - [Jetty](#jetty)
  - [HTTP Kit](#http-kit)
- [Screenshot](#screenshot)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Installation

Leiningen:

```
[com.buckryan/farmhand-ui "0.3.1"]
```

## Usage

This library is meant to be use within an existing application running
Farmhand. This is not necessary, however, and you are free to run it in a
separate process. For now, we will assume you are going to run it inside your
existing application.

The `farmhand.ui.handler` namespace contains one main function for creating a
handler that can be used with Jetty, HTTP Kit, etc. Here is what the basic code
will look like to create an app:

```clojure
(ns my.namespace
  (:require [farmhand.core :as farmhand]
            [farmhand.ui.handler :as farmhand-ui]))

;; Start the Farmhand server before creating the handler
(farmhand/start-server)

;; Now create the handler
(def handler (farmhand-ui/app))

;; Now you need to start an HTTP server. See the notes below depending on the
;; server you are using.
```

### Jetty

```clojure
;; Using the handler created above,
(ring.adapter.jetty/run-jetty handler {})
```

See more details on `run-jetty`
[here](http://ring-clojure.github.io/ring/ring.adapter.jetty.html#var-run-jetty).

### HTTP Kit

```clojure
;; Using the handler created above,
(org.httpkit.server/run-server handler)
```

More documentation can be found
[here](http://www.http-kit.org/server.html#stop-server).

## Screenshot

![Screenshot](https://github.com/b-ryan/farmhand-ui/raw/master/preview.png)
