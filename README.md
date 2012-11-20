# shafty

ClojureScript functional reactive programming library. Shafty is just an experiment and shouldn't be used in production.  Shafty is currently in development.

Get in touch if you are interested in helping out.

## Usage

### Events

Events represent time-varying functions which have a finite set of
occurences over time.

```clojure
(def my-event (shafty/event))
```

Compose event streams using filter!, map!, reduce!, etc.

```clojure
(def my-filtered-event (shafty/filter! my-event
                                (fn [x] (= 1 x))))

(def my-mapped-event (shafty/map! my-event
                           (fn [x] (identity 1))))

(def my-combo-event (shafty/merge! my-filtered-event
                                   my-mapped-event))

(def my-delayed-event (shafty/delay! my-filtered-event 50000)
```

### Behaviours

Behaviours are time-varying functions which constantly have a value.
Derive a behaviour from an event, or generate a receiver to watch a
particular DOM element, such as an input field.

Behaviours share the same IEventStream protocol, so you can also use
merge!, delay!, map!, filter!, etc. to compose them.

You can also call changes! to convert a Behaviour back to an Event.

```clojure
(def my-behaviour-of-ones (shafty/hold! my-filtered-event 1))

(def my-behaviour-as-event (shafty/changes! my-behaviour-of-ones))
```

## Examples

The repository contains a series of examples in the
```shafty.examples``` namespace.  Included below is a simple example of
a Google-docs like autosave feature built using shafty.

```clojure
(ns shafty.examples.autosave
  (:use [shafty.observable :only [bind! bind-timer! bind-behaviour!]]
        [shafty.event-stream :only [merge! map!]])
  (:require [goog.dom :as dom]))

(defn- update-save-status []
  (let [element (dom/getElement "save-status")
        curtime (js/Date)
        textarea (deref b1)]
    (set! (.-innerHTML element) (str "Last save at " curtime))))

(defn main []
  "Run the autosave example"

  (let [e1 (bind-timer! 5000)
        e2 (bind! (dom/getElement "save-button") "click")
        e3 (merge! e1 e2)
        e4 (map! e3 update-save-status)
        b1 (bind! (dom/getElement "data"))]
    (.log js/console "Running autosave example.")))
```

## References

* Elliott, Conal., "Push-Pull Functional Reactive Programming"
* Meyerovich, Leo., "Flapjax: Functional Reactive Web Programming"
* Meyerovich, Leo., "Flapjax: A Programming Language for Ajax Applications"

## License

Copyright (C) 2012 Christopher Meiklejohn.

Distributed under the Eclipse Public License, the same as Clojure.
