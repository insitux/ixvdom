
(function view state
  [:section
    [:p "Count: " [:b state]]
    [:p [:button {:onclick increment} "Increment"]]])

(ixvdom-mount "body" view (or $state 0))

(function increment
  ($state (inc $state)))
