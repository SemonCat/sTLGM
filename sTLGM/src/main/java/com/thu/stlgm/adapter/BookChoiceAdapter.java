package com.thu.stlgm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.thu.stlgm.R;

import org.androidannotations.helper.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/19.
 */
public class BookChoiceAdapter extends BaseAdapter{

    private static final String TAG = BookChoiceAdapter.class.getName();

    private int bookresource[] = new int[]{R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5};
    private String booktitle[] = new String[]{"Where Good Ideas Come From","賴聲川的創意學","inGenius: A Crash Course on Creativity\n學創意, 現在就該懂的事","Business Model Generation\n獲利世代：自己動手，畫出你的商業模式","The Lean Startup\n精實創業"};

    private boolean bookanswers[][] = new boolean[][]{{true,false,false},
                                                      {true,false,false},
                                                      {true,true,false},
                                                      {false,false,true},
                                                      {false,false,true},};


    private String options[] = new String[]{"創意","創新","創業"};

    private Context mContext;

    private boolean IsAnswer = false;

    private List<Book> mDatas;

    public BookChoiceAdapter(Context context) {
        this.mContext = context;
        mDatas = getBookBean();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Book getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position,View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_bookchoice,
                            parent, false);

            holder = new ViewHolder();
            holder.BookCover = (ImageView)convertView.findViewById(R.id.BookCover);
            holder.BookTitle = (TextView) convertView.findViewById(R.id.BookTitle);


            holder.ListOptions = (ListView) convertView.findViewById(R.id.ListOptions);
            holder.ListOptions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            ArrayAdapter mAdapter = new ArrayAdapter(
                    mContext
                    , android.R.layout.simple_list_item_single_choice
                    , Arrays.asList(options)
            );

            holder.ListOptions.setAdapter(mAdapter);

            holder.ListOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    CheckedTextView mCheckView = ((CheckedTextView)view);

                    List<Option> mOptions = getItem(position).Answers;
                    mOptions.get(i).UserAnswers = mCheckView.isChecked();

                }
            });

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.BookCover.setImageResource(bookresource[position]);
        holder.BookTitle.setText((getItem(position).id+1)+". "+booktitle[position]);

        if (IsAnswer){
            resetAnswer();
            for (Option mOption : getItem(position).Answers){
                holder.ListOptions.setItemChecked(mOption.id,mOption.Answer);
            }
        }

        return convertView;
    }

    public List<Book> checkAnswer(){
        this.IsAnswer = true;
        for (Book mBook:mDatas)
            mBook.checkAnswer();
        notifyDataSetChanged();

        return mDatas;
    }

    private void resetAnswer(){
        for (Book mBook:mDatas)
            mBook.resetAnswer();
    }

    private View getListItemView(ListView listView,int wantedPosition){
        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
        int wantedChild = wantedPosition - firstPosition;
        // Say, first visible position is 8, you want position 10, wantedChild will now be 2
        // So that means your view is child #2 in the ViewGroup:
        if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
            Log.w(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
            return null;
        }
        // Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
        View wantedView = listView.getChildAt(wantedChild);

        return wantedView;
    }

    private List<Book> getBookBean(){
        List<Book> mBooks = new ArrayList<Book>();
        for (int i=0;i<bookresource.length;i++){
            Book mBook = new Book();
            mBook.id = i;
            mBook.Cover = bookresource[i];
            mBook.Title = booktitle[i];

            List<Option> mOptions = new ArrayList<Option>();
            for (int j=0;j<options.length;j++){
                Option mOption = new Option();
                mOption.id = j;
                mOption.Name = options[j];
                mOption.Answer = bookanswers[i][j];
                mOption.UserAnswers = false;
                mOptions.add(mOption);
            }

            mBook.Answers = mOptions;
            mBooks.add(mBook);
        }

        return mBooks;
    }


    class ViewHolder{
        ImageView BookCover;
        TextView BookTitle;
        ListView ListOptions;
    }

    public class Book{
        public int id;
        int Cover;
        String Title;
        List<Option> Answers = new ArrayList<Option>();
        public  AnswerState AnswerState = BookChoiceAdapter.AnswerState.NOT_COMPLETE;

        public void checkAnswer(){
            for (Option mOption:Answers)
                if (mOption.Answer!=mOption.UserAnswers){
                    AnswerState = BookChoiceAdapter.AnswerState.WRONG;
                    return;
                }

            AnswerState = BookChoiceAdapter.AnswerState.CURRENT;

        }

        public void resetAnswer(){
            AnswerState = BookChoiceAdapter.AnswerState.NOT_COMPLETE;
            for (Option mOption:Answers)
                mOption.UserAnswers = false;
        }
    }

    class Option{
        int id;
        String Name;
        boolean Answer;
        boolean UserAnswers;
    }

    public enum AnswerState{
        CURRENT,
        WRONG,
        NOT_COMPLETE
    }
}


