package vitaliqp.shootballscreen.net;

import android.content.Context;

import vitaliqp.shootballscreen.datas.Constant;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/9 下午1:52
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MinaConnectionConfig {
    private Context mContext;
    private String mIp;
    private int mPort;
    private int mReadBufferSize;
    private long mConnectionTimeout;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getIp() {
        return mIp == null ? "" : mIp;
    }

    public void setIp(String ip) {
        this.mIp = ip;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        this.mPort = port;
    }

    public int getReadBufferSize() {
        return mReadBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.mReadBufferSize = readBufferSize;
    }

    public long getConnectionTimeout() {
        return mConnectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.mConnectionTimeout = connectionTimeout;
    }

    public static class Builder {
        private Context Context;
        private String ip = Constant.IP;
        private int port = Constant.PORT;
        private int readBufferSize = Constant.READ_BUFFER_SIZE;
        private long connectionTimeout = Constant.CONNECT_TIMEOUT;

        public Builder(Context context) {
            Context = context;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public Builder setConnectTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public MinaConnectionConfig builder() {
            MinaConnectionConfig config = new MinaConnectionConfig();
            applyConfig(config);
            return config;
        }

        public void applyConfig(MinaConnectionConfig config) {
            config.mContext = this.Context;
            config.mIp = this.ip;
            config.mPort = this.port;
            config.mReadBufferSize = this.readBufferSize;
            config.mConnectionTimeout = this.connectionTimeout;
        }
    }
}
