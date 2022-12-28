package account.dtos;

public enum LockOperation {
    LOCK(true, "Lock user ", Action.LOCK_USER), UNLOCK(false, "Unlock user ", Action.UNLOCK_USER);
    private final boolean lockStatus;
    private final String eventMsg;
    private final Action action;



    LockOperation(boolean lockStatus, String eventMsg, Action action) {
        this.lockStatus = lockStatus;
        this.eventMsg = eventMsg;
        this.action = action;
    }

    public boolean getLockStatus() {
        return lockStatus;
    }

    public String getEventMsg() {
        return eventMsg;
    }

    public Action getAction() {
        return action;
    }
}
