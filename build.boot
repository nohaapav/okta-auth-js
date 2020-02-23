(set-env!
 :resource-paths #{"resources"}
 :dependencies '[[cljsjs/boot-cljsjs "0.10.5" :scope "test"]
                 [cljsjs/react "16.8.6-0"]
                 [cljsjs/react-dom "16.8.6-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "2.13.0")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom {:project     'cljsjs/okta-auth-js
      :version     +version+
      :description "The official js wrapper around Okta's auth API"
      :url         "http://www.okta.com/"
      :scm         {:url "https://github.com/nohaapav/okta-auth-js"}
      :license     {"Okta Auth SDK License" "https://github.com/okta/okta-auth-js/blob/master/LICENSE"}})

(deftask package []
  (comp
   (run-commands :commands [["npm" "install" "--include-dev"]
                            ["npm" "run" "build:dev"]
                            ["npm" "run" "build:prod"]
                            ["rm" "-rf" "./node_modules"]])
   (sift :move {#".*okta-auth-js.inc.js"     "cljsjs/okta-auth-js/development/okta-auth-js.inc.js"
                #".*okta-auth-js.min.inc.js" "cljsjs/okta-auth-js/production/okta-auth-js.min.inc.js"})
   (sift :include #{#"^cljsjs"})
   (deps-cljs :foreign-libs [{:file           #"okta-auth-js.inc.js"
                              :file-min       #"okta-auth-js.min.inc.js"
                              :provides       ["@okta/okta-auth-js"]
                              :global-exports '{"@okta/okta-auth-js" OktaAuth}
                              :requires       ["react" "react-dom"]}]
              :externs [#"okta-auth-js.ext.js"])
   (pom)
   (jar)
   (validate-checksums)))
