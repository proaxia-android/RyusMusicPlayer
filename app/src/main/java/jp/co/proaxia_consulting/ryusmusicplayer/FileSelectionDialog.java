package jp.co.proaxia_consulting.ryusmusicplayer;

import java.util.List;
import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2015/01/16.
 */
public class FileSelectionDialog
        implements OnItemClickListener {

    private Context m_parent;                // 呼び出し元
    private OnFileSelectListener m_listener;            // 結果受取先
    private AlertDialog m_dlg;                    // ダイアログ
    private FileInfoArrayAdapter m_fileinfoarrayadapter; // ファイル情報配列アダプタ

    // コンストラクタ
    public FileSelectionDialog(Context context,
                               OnFileSelectListener listener) {
        m_parent = context;
        m_listener = (OnFileSelectListener) listener;
    }

    // ダイアログの作成と表示
    public void show(File fileDirectory) {
        // タイトル
        String strTitle = fileDirectory.getAbsolutePath();

        // リストビュー
        ListView listview = new ListView(m_parent);
        listview.setScrollingCacheEnabled(false);
        listview.setOnItemClickListener(this);
        // ファイルリスト
        File[] aFile = fileDirectory.listFiles();
        List<FileInfo> listFileInfo = new ArrayList<FileInfo>();
        if (null != aFile) {
            for (File fileTemp : aFile) {
                listFileInfo.add(new FileInfo(fileTemp.getName(), fileTemp));
            }
            Collections.sort(listFileInfo);
        }
        // 親フォルダに戻るパスの追加
        if (null != fileDirectory.getParent()) {
            listFileInfo.add(0, new FileInfo("..", new File(fileDirectory.getParent())));
        }
        m_fileinfoarrayadapter = new FileInfoArrayAdapter(m_parent, listFileInfo);
        listview.setAdapter(m_fileinfoarrayadapter);

        Builder builder = new AlertDialog.Builder(m_parent);
        builder.setTitle(strTitle);
        builder.setPositiveButton("Cancel", null);
        builder.setView(listview);
        m_dlg = builder.show();
    }

    // ListView内の項目をクリックしたときの処理
    public void onItemClick(AdapterView<?> l,
                            View v,
                            int position,
                            long id) {
        if (null != m_dlg) {
            m_dlg.dismiss();
            m_dlg = null;
        }

        FileInfo fileinfo = m_fileinfoarrayadapter.getItem(position);

        if (true == fileinfo.getFile().isDirectory()) {
            show(fileinfo.getFile());
        } else {
            // ファイルが選ばれた：リスナーのハンドラを呼び出す
            m_listener.onFileSelect(fileinfo.getFile());
        }
    }

    // 選択したファイルの情報を取り出すためのリスナーインターフェース
    public interface OnFileSelectListener {
        // ファイルが選択されたときに呼び出される関数
        public void onFileSelect(File file);
    }


    //------------------------------------------------------
    class FileInfo
            implements Comparable<FileInfo> {
        private String m_strName;    // 表示名
        private File m_file;    // ファイルオブジェクト

        // コンストラクタ
        public FileInfo(String strName,
                        File file) {
            m_strName = strName;
            m_file = file;
        }

        public String getName() {
            return m_strName;
        }

        public File getFile() {
            return m_file;
        }

        // 比較
        public int compareTo(FileInfo another) {
            // ディレクトリ < ファイル の順
            if (true == m_file.isDirectory() && false == another.getFile().isDirectory()) {
                return -1;
            }
            if (false == m_file.isDirectory() && true == another.getFile().isDirectory()) {
                return 1;
            }

            // ファイル同士、ディレクトリ同士の場合は、ファイル名（ディレクトリ名）の大文字小文字区別しない辞書順
            return m_file.getName().toLowerCase().compareTo(another.getFile().getName().toLowerCase());
        }

    }

    class FileInfoArrayAdapter extends ArrayAdapter<FileInfo> {
        private List<FileInfo> m_listFileInfo; // ファイル情報リスト

        // コンストラクタ
        public FileInfoArrayAdapter(Context context,
                                    List<FileInfo> objects) {
            super(context, -1, objects);

            m_listFileInfo = objects;
        }

        // m_listFileInfoの一要素の取得
        @Override
        public FileInfo getItem(int position) {
            return m_listFileInfo.get(position);
        }

        // 一要素のビューの生成
        @Override
        public View getView(int position,
                            View convertView,
                            ViewGroup parent) {
            // レイアウトの生成
            if (null == convertView) {
                Context context = getContext();
                // レイアウト
                LinearLayout layout = new LinearLayout(context);
                layout.setPadding(10, 10, 10, 10);
                layout.setBackgroundColor(Color.WHITE);
                convertView = layout;
                // テキスト
                TextView textview = new TextView(context);
                textview.setTag("text");
                textview.setTextColor(Color.BLACK);
                textview.setPadding(10, 10, 10, 10);
                layout.addView(textview);
            }

            // 値の指定
            FileInfo fileinfo = m_listFileInfo.get(position);
            TextView textview = (TextView) convertView.findViewWithTag("text");
            if (fileinfo.getFile().isDirectory()) { // ディレクトリの場合は、名前の後ろに「/」を付ける
                textview.setText(fileinfo.getName() + "/");
            } else {
                textview.setText(fileinfo.getName());
            }

            return convertView;
        }
    }
}
