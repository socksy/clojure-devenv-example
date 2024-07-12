(ns build
  (:require [clojure.tools.deps :as t]
            [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'io.github.socksy/clojure-devenv-example)
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def uber-file (format "target/%s-%s-standalone.jar" lib version))
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn uberjar "Build the uberjar" [opts]
  (b/delete {:path "target"})
  (b/copy-dir {:src-dirs ["resources" "src"] 
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'clojure-devenv-example.main}))
