package jp.co.proaxia_consulting.ryusmusicplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends ActionBarActivity
        implements FileSelectionDialog.OnFileSelectListener{
    private String str = "";
    private VideoView m_Video;
    private File m_sdDir;
    private String m_strInitialDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Video = (VideoView)findViewById(R.id.videoView);
        // メディアコントローラーの設定
        MediaController mc = new MediaController(this);
        m_Video.setMediaController(mc);
        // sdcardフォルダを指定
        m_sdDir = Environment.getExternalStorageDirectory();
        m_strInitialDir = m_sdDir.getPath();
/*
        //&#x52d5;&#x753b;&#x30e1;&#x30c7;&#x30a3;&#x30a2;&#x306e;&#x8a2d;&#x5b9a;
        // sdcard&#x304b;&#x3089;&#x52d5;&#x753b;&#x518d;&#x751f;
//        myVideo.setVideoPath("/sdcard/free_music01.mp3");
        m_Video.setVideoPath(m_sdDir.toURI() + "free_mv01.3gp");

        m_Video.start();
        mc.show();
*/
/*
        Button btn = (Button)findViewById(R.id.btnOpen);
        btn.setText("音楽再生に切り替え");
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//----------------------------------------------------------------------------
/*
    public void openContent(View v){
        Button btn=(Button)v;
        //showDialog(btn.getText().toString());

        m_Video.stopPlayback(); //再生停止
        if("音楽再生に切り替え".equals(btn.getText())){
//            myVideo.setVideoPath("/sdcard/free_music01.mp3");
            m_Video.setVideoPath(m_sdDir.toURI() + "free_music01.mp3");
            btn.setText("動画再生に切り替え");
        }else{
//            myVideo.setVideoPath("/sdcard/free_mv01.3gp");
//            myVideo.setVideoPath("/sdcard/free_mv02.3gp");
//            myVideo.setVideoPath("/sdcard/youtube01.3gp");
            m_Video.setVideoPath(m_sdDir.toURI() + "youtube01.3gp");
            btn.setText("音楽再生に切り替え");
        }
        m_Video.seekTo(0);
        m_Video.start();    //再生開始

    }
*/
    //アプリ終了ボタン
    public void finishApl(View v){
        m_Video.stopPlayback(); //再生停止

        this.finish();
    }
    //ファイル選択ボタン
    public void selectFile(View v){
        // ダイアログオブジェクト
        FileSelectionDialog dlg = new FileSelectionDialog( this, this );
        dlg.show( new File( m_strInitialDir ) );
        return;
    }
    // ファイルが選択されたときに呼び出される関数
    public void onFileSelect( File file )
    {
        m_Video.stopPlayback(); //再生停止
        m_strInitialDir = file.getParent();
        Toast.makeText( this, "File Selected : " + file.getPath() + " , " +
                "ParentDir : "+m_strInitialDir, Toast.LENGTH_SHORT ).show();

        m_Video.setVideoPath(file.getPath());
        m_Video.start();    //再生開始
        //
        TextView tv = (TextView) findViewById(R.id.txtFname);
        tv.setText(file.getName());
    }
    /**
     * Dialogを表示
     *
     * @param msg 表示するメッセージ
     */
    private void showDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                    // この中に"YES"時の処理を入れる。
                    public void onClick(DialogInterface dialog, int whichButton) {
                        str="YES";
                    }
                }).show();
    }

}
