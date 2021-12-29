;https://medium.com/@deathmood/how-to-write-your-own-virtual-dom-ee74acc13060

(function from-key (-> % str sect))
(function vec->html v
  (if! (vec? v) (return v))
  (let [tag attr] v
       has-attr   (dict? attr)
       make-attr  (fn [k v] (str " " (from-key k) "=\"" v "\""))
       attr       (if has-attr (map make-attr attr) "")
       tag        (from-key tag)
       body       (sect v (has-attr 2 1))
       body       (map vec->html body))
  (.. str "<" tag attr ">" body "</" tag ">"))

(var -old-dom [])

(function make-el x
 (if (vec? x)
  (new-el (0 x) (vec->html (sect x)))
  (new-text-el x)))

(function el= old new
  (if (and (vec? old) (vec? new))
    (= (0 old) (0 new))
    (= old new)))

(function children el
  (let sans-tag (sect el))
  (if (dict? (0 sans-tag)) 
    (sect sans-tag)
    sans-tag))

(function update-children&props parent old new index
  ;(match true
  ;  (dict? (1 old))
  ;    )
  (-> @(update-walk (child-at parent index))
      #(map % (children old)
              (children new)
              (range (len old)))))

(function update-walk parent old new index
(print parent " " old " " new " " index)
  (match true
    (! old)
      (append-child parent (make-el new))
    (! new)
      (remove-child parent index)
    (! (el= old new))
      (replace-child parent (make-el new) index)
    (vec? old)
      (update-children&props parent old new index)))

(function update
  (let new-dom (-view $state))
  (print "old dom " -old-dom)
  (print "new dom " new-dom)
  (update-walk $mount -old-dom new-dom 0)
  (var -old-dom new-dom))

(function ixvdom-mount mount view state
  ($mount mount)
  (var -view view)
  ($state (or $state state))
  (-> state view (var -old-dom) vec->html (html mount)))

(ixvdom-mount "body" @[:h2 {:onclick "blah"} [:span "Count: "]] 1)

(function next
  ($state (inc $state))
  (update))

(interval next 1000)