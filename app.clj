
(function ix func
  (str "ix('(" func ") (update)')"))

(function view state
  [:section
    [:p "Count: " [:b state]]
    [:p [:button {:onclick (ix increment)} "Increment"]]])

(ixvdom-mount "body" view 0)

(function increment
  ($state (inc $state)))
