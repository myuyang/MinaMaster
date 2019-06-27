package vitaliqp.shootballscreen.datas;

/**
 * 类名：vitaliqp.shootballscreen.interfaces
 * 时间：2018/11/28 下午2:30
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class ShootBallConfiguration {

    private int elevationAngular;
    private int horizontalAngular;
    private int distance;
    private int frequency;

    public int getHorizontalAngular() {
        return horizontalAngular;
    }

    public void setHorizontalAngular(int horizontalAngular) {
        this.horizontalAngular = horizontalAngular;
    }

    public int getElevationAngular() {
        return elevationAngular;
    }

    public void setElevationAngular(int elevationAngular) {
        this.elevationAngular = elevationAngular;
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
