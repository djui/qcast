(ns qcast.feed.atom)


;;; Globals

(def spec
  {:feed []
   :entry []})


;;; Utilities

(defn- extension []
  (throw (UnsupportedOperationException. "Not yet implemented")))

(defn create
  "Create an Atom Syndication 1.0 feed.
  Specification: http://tools.ietf.org/html/rfc4287"
  [meta elements & [extensions]]
  (throw (UnsupportedOperationException. "Not yet implemented")))
