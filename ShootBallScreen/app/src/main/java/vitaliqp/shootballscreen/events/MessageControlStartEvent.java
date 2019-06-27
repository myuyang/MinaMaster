package vitaliqp.shootballscreen.events;

/**
 * 类名：vitaliqp.shootballscreen.events
 * 时间：2018/12/12 下午4:50
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MessageControlStartEvent {

    private boolean mIsRunning;

    public MessageControlStartEvent(boolean isRunning) {
        mIsRunning = isRunning;
    }

    public boolean istart() {
        return mIsRunning;
    }

    public void setRunning(boolean running) {
        mIsRunning = running;
    }
}
