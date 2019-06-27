package vitaliqp.shootballscreen.fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * 类名：vitaliqp.shootballscreen.fragments
 * 时间：2018/11/30 上午9:52
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null){
            //需要在fragment中创建menu
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
