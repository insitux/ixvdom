
(function view state
  [:section
    [:p "Count: " [:b state]]
    [:p [:button {:onclick increment} "Increment"]]])

(ixvdom-mount "body" view $state)

(function increment
  ($state (inc $state)))
