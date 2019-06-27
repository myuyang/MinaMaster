package vitaliqp.shootballscreen.datas;

import java.util.List;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2019/4/18 上午10:10
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class SendJsonToServer {

    /**
     * type : 0
     */

    @MinaControlMode
    private int type;
    /**
     * state : 0
     * remote : [{"roller":0,"lin":0,"ang":0}]
     * auto : [{"width":0,"height":0}]
     * shoot : [{"elevationAngular":0,"horizontalAngular":0,"distance":0,"frequency":0}]
     */

    private int state;
    private List<RemoteBean> remote;
    private List<AutoBean> auto;
    private List<ShootBean> shoot;

    @MinaControlMode
    public int getType() {
        return type;
    }

    public void setType(@MinaControlMode int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<RemoteBean> getRemote() {
        return remote;
    }

    public void setRemote(List<RemoteBean> remote) {
        this.remote = remote;
    }

    public List<AutoBean> getAuto() {
        return auto;
    }

    public void setAuto(List<AutoBean> auto) {
        this.auto = auto;
    }

    public List<ShootBean> getShoot() {
        return shoot;
    }

    public void setShoot(List<ShootBean> shoot) {
        this.shoot = shoot;
    }


    public static class RemoteBean {
        /**
         * roller : 0
         * lin : 0
         * ang : 0
         */

        private int roller;
        private int lin;
        private int ang;

        public int getRoller() {
            return roller;
        }

        public void setRoller(int roller) {
            this.roller = roller;
        }

        public int getLin() {
            return lin;
        }

        public void setLin(int lin) {
            this.lin = lin;
        }

        public int getAng() {
            return ang;
        }

        public void setAng(int ang) {
            this.ang = ang;
        }
    }

    public static class AutoBean {
        /**
         * width : 0
         * height : 0
         */

        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class ShootBean {
        /**
         * elevationAngular : 0
         * horizontalAngular : 0
         * distance : 0
         * frequency : 0
         */

        private int elevationAngular;
        private int horizontalAngular;
        private int distance;
        private int frequency;

        public int getElevationAngular() {
            return elevationAngular;
        }

        public void setElevationAngular(int elevationAngular) {
            this.elevationAngular = elevationAngular;
        }

        public int getHorizontalAngular() {
            return horizontalAngular;
        }

        public void setHorizontalAngular(int horizontalAngular) {
            this.horizontalAngular = horizontalAngular;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}
