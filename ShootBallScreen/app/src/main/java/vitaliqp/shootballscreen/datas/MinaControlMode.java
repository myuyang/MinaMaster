package vitaliqp.shootballscreen.datas;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2019/4/18 下午2:13
 * 描述：CLASS ： 编译器要丢弃的注释、被编译器忽略！
 *      RUNTIME ：编译器将把注释记录在类文件中，在运行时 VM 将保留注释，因此可以反射性地读取。
 *      SOURCE ：编译器将把注释记录在类文件中，但在运行时 VM 不需要保留注释。这是默认的行为。
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
@IntDef({Constant.MODE_STAND_BY, Constant.MODE_REMOTE, Constant.MODE_AUTO, Constant.MODE_SHOOT})
@Retention(RetentionPolicy.SOURCE)
public @interface MinaControlMode {

}
