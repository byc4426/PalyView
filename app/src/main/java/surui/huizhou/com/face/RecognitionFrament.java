package surui.huizhou.com.face;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import surui.huizhou.com.palycontrol.CircleBar33;
import surui.huizhou.com.palycontrol.OnPlayListener;

public class RecognitionFrament extends Fragment {
    private android.widget.Button sss;
    private CircleBar33 ccc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament1, null);
        this.ccc = (CircleBar33) view.findViewById(R.id.ccc);


        ccc.setOnPlayListener(new OnPlayListener() {
            @Override
            public void pauseListener(boolean pause) {
                Toast.makeText(getActivity(), pause ? "播放" : "暂停", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void muteListener(boolean mute) {
                Toast.makeText(getActivity(), mute ? "静音" : "开启声音", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void volumeListener(double volume) {
                Toast.makeText(getActivity(), "音量：" + volume, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void progressListener(double total, double progress, double percent) {
                Toast.makeText(getActivity(), "进度:" + progress + "---" + percent + "%", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void speedListener() {
                Toast.makeText(getActivity(), "快进", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void backListener() {
                Toast.makeText(getActivity(), "后退", Toast.LENGTH_SHORT).show();
            }
        });
//        sss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ccc.setProgress(150f);
//                ccc.setVol(30.0f);
//            }
//        });
        Log.e("onCreateView", "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("onViewCreated", "onViewCreated");
    }


}
