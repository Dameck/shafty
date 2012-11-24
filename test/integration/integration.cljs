;; Copyright (c) Christopher Meiklejohn. All rights reserved.
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0
;; (http://opensource.org/licenses/eclipse-1.0.php) which can be found
;; in the file LICENSE.html at the root of this distribution.  By using
;; this software in any fashion, you are agreeing to be bound by the
;; terms of this license. You must not remove this notice, or any other,
;; from this software.
;;
(ns test.integration.integration
  (:use [shafty.event :only [event]]
        [shafty.event-stream :only [merge! map! filter!]]
        [shafty.behaviour-conversion :only [hold!]]))

(.log js/console "Starting Tests")

; Generate a series of events, and verify that after all changes have
; propgated a filtered event only contains the correct values.
;
 (let [e1 (event)
       e2 (filter! e1 (fn [x] (= 1 x)))
       e3 (map! e2 (fn [x] (assert (= 1 x))))]
   (-notify-watches e1 nil 2)
   (-notify-watches e1 nil 1)
   (-notify-watches e1 nil 1))

;; Generate a series of events, and verify that after all changes have
;; propgated a mapped event only contains the correct values.
;;
(let [e1 (event)
      e2 (map! e1 (fn [x] (identity 3)))
      e3 (map! e2 (fn [x] (assert (= 3 x))))]
  (-notify-watches e1 nil 2)
  (-notify-watches e1 nil 1)
  (-notify-watches e1 nil 1))

;; Generate a series of events, hold to a behaviour, and assert that the
;; behaviour deref's to the correct value.
;;
(let [e1 (event)
      e2 (map! e1 (fn [x] (identity 3)))
      b1 (hold! e2 nil)]
  (-notify-watches e1 nil 2)
  (-notify-watches e1 nil 1)
  (-notify-watches e1 nil 1)
  (assert (= 3 @b1)))

;; Generate multiple event streams, merge into one, hold into a
;; behaviour and verify that all events are received.
;;
(let [e1 (event)
      e2 (event)
      e3 (merge! e1 e2)
      b1 (hold! e3 0)]
  (-notify-watches e1 nil 1)
  (assert (= 1 @b1))
  (-notify-watches e2 nil 2)
  (assert (= 2 @b1))
  (-notify-watches e1 nil 3)
  (assert (= 3 @b1))
  (-notify-watches e2 nil 4)
  (assert (= 4 @b1)))

(.log js/console "Ending Tests")
