(ns onyx.peer.pipeline-extensions
  "Public API extensions for the virtual peer data pipeline.")

(defn ident-dispatch [{:keys [onyx.core/task-map]}]
  (:onyx/ident task-map))

(defmulti ^{:added "0.6.0"} read-batch
  "Reads :onyx/batch-size segments off the incoming data source.
   Must return a map with key :onyx.core/batch and value seq representing
   the ingested segments. The seq must be maps of two keys:

   - :input - A keyword representing the task that the message came from
   - :message - The consumed message"
  ident-dispatch)

(defmulti ^{:added "0.6.0"} write-batch
  "Writes segments to the outgoing data source. Must return a map."
  ident-dispatch)

(defmulti ^{:added "0.6.0"} seal-resource
  "Closes any resources that remain open during a task being executed.
   Called once at the end of a task for each virtual peer after the incoming
   queue has been exhausted. Only called once globally for a single task."
  ident-dispatch)

(defmulti ^{:added "0.6.0"} ack-message
  "Acknowledges a message at the native level for a batch of message ids.
   Must return a map."
  (fn [event message-id]
    (ident-dispatch event)))

(defmulti ^{:added "0.6.0"} retry-message
  "Releases a message id from the pending message pool and retries it."
  (fn [event message-id]
    (ident-dispatch event)))

(defmulti ^{:added "0.6.0"} pending?
  "Returns true if this message ID is pending."
  (fn [event messsage-id]
    (ident-dispatch event)))

(defmulti ^{:added "0.6.0"} drained?
  "Returns true if this input resource has been exhausted."
  ident-dispatch)
