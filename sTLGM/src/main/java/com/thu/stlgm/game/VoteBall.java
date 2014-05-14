package com.thu.stlgm.game;

import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thu.stlgm.R;
import com.thu.stlgm.component.BallView;
import com.thu.stlgm.component.BookLayout;
import com.thu.stlgm.component.BookView;
import com.thu.stlgm.fragment.BaseFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/21.
 */
@EFragment(R.layout.fragment_ball)
public class VoteBall extends BaseGame {

    private static final String TAG = VoteBall.class.getName();

    class Question{
        private int id;
        private int drawable;
        private List<Answer> answerList;

        Question(int id, int drawable) {
            this.id = id;
            this.drawable = drawable;
        }

        Question(int id, int drawable, List<Answer> answers) {
            this.id = id;
            this.drawable = drawable;
            this.answerList = answers;
        }

        Question(int id, int drawable, Answer[] answers) {
            this.id = id;
            this.drawable = drawable;
            this.answerList = Arrays.asList(answers);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        public List<Answer> getAnswerList() {
            return answerList;
        }

        public Answer[] getAnswerArray() {
            return answerList.toArray(new Answer[answerList.size()]);
        }

        public void setAnswerList(List<Answer> answerList) {
            this.answerList = answerList;
        }

        public boolean IsAnswer(Answer mAnswer){
            return this.answerList.contains(mAnswer);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Question)) return false;

            Question question = (Question) o;

            if (id != question.id) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + drawable;
            result = 31 * result + (answerList != null ? answerList.hashCode() : 0);
            return result;
        }
    }

    class Answer{
        private int id;
        private int drawable;

        Answer(int id, int drawable) {
            this.id = id;
            this.drawable = drawable;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Answer)) return false;

            Answer answer = (Answer) o;

            if (drawable != answer.drawable) return false;
            if (id != answer.id) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + drawable;
            return result;
        }
    }

    @ViewById
    RelativeLayout container;


    private BallView mBallView;

    @ViewById
    ImageView Start;

    @ViewById
    ImageView Reciprocal;

    //@ViewById
    //ImageView book1,book2,book3,book4,book5;

    @ViewById
    BookLayout BookLayout;

    @ViewById
    ImageView centerIcon;

    private boolean[] answer = new boolean[]{true, true, true, false, false, true};
    private boolean[] userAnswer = new boolean[5];

    private int bookdrawables[][] = new int[][]
            {{R.drawable.book1, R.drawable.book1_g, R.drawable.book1_r},
                    {R.drawable.book2, R.drawable.book2_g, R.drawable.book2_r},
                    {R.drawable.book3, R.drawable.book3_g, R.drawable.book3_r},
                    {R.drawable.book4, R.drawable.book4_g, R.drawable.book4_r},
                    {R.drawable.book5, R.drawable.book5_g, R.drawable.book5_r},};

    private int type1Drawale[][] = new int[][]
            {{R.drawable.hp, R.drawable.ebay, R.drawable.google, R.drawable.intel, R.drawable.youtube},
                    {R.drawable.amazon, R.drawable.intel, R.drawable.hp, R.drawable.apple, R.drawable.youtube},
                    {R.drawable.google, R.drawable.apple, R.drawable.amazon, R.drawable.microsoft, R.drawable.youtube},
                    {R.drawable.ebay, R.drawable.hp, R.drawable.fb, R.drawable.intel, R.drawable.youtube},};

    private int type1Logo[] = new int[]{
            R.drawable.larryandsergey, R.drawable.jobs, R.drawable.bill, R.drawable.mark};

    private boolean type1answer[][] = new boolean[][]
            {{false, false, true, false, false},
                    {false, false, false, true, false},
                    {false, false, false, true, false},
                    {false, false, true, false, false},};

    private int trueDrawable[] = new int[]{R.drawable.opt_m43_t_01,
            R.drawable.opt_m43_t_02,
            R.drawable.opt_m43_t_03};

    private int falseDrawable[] = new int[]{R.drawable.opt_m43_f_01,
            R.drawable.opt_m43_f_02,
            R.drawable.opt_m43_f_03};

    private int questionDrawable[] = new int[]{
            R.drawable.opt_m52_q_01,
            R.drawable.opt_m52_q_02,
            R.drawable.opt_m52_q_03,
            R.drawable.opt_m52_q_04,
            R.drawable.opt_m52_q_05,
    };

    private int answerDrawable[] = new int[]{
            R.drawable.opt_m52_a_01,
            R.drawable.opt_m52_a_02,
            R.drawable.opt_m52_a_03,
            R.drawable.opt_m52_a_04
    };

    private Answer mAnswer[] = new Answer[]{
            new Answer(0,R.drawable.opt_o_01),
            new Answer(1,R.drawable.opt_o_02),
            new Answer(2,R.drawable.opt_o_03),
            new Answer(3,R.drawable.opt_o_04),
    };

    private Question mQuestion[] = new Question[]{
            new Question(0,R.drawable.opt_m52_q_01,new Answer[]{mAnswer[0],mAnswer[1],mAnswer[2],mAnswer[3]}),
    };




    @ViewById
    FrameLayout StartFrame;

    @AnimationRes
    Animation push_right_to_left;

    private int ballview = R.anim.obj_m_ball03;
    private int background = R.drawable.bkg_m_06;

    private int Type;

    private int score = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Init();

    }


    public void setupType(int type) {
        this.Type = type;

        switch (type) {
            case 0:

                ballview = R.anim.obj_m_ball03;

                break;
            case 54:
                mAnswer = new Answer[]{
                        new Answer(0,R.drawable.opt_m54_q_01),
                        new Answer(1,R.drawable.opt_m54_q_02),
                        new Answer(2,R.drawable.opt_m54_q_03),
                        new Answer(3,R.drawable.opt_m54_q_04),
                        new Answer(3,R.drawable.opt_m54_q_05),
                };

                mQuestion = new Question[]{
                        new Question(0,R.drawable.opt_m54_a_01,new Answer[]{mAnswer[0],mAnswer[1],mAnswer[2]}),
                        new Question(1,R.drawable.opt_m54_a_02,new Answer[]{mAnswer[3],mAnswer[4]}),
                };
                ballview = R.anim.obj_m_ball02;
                background  = R.drawable.bkg_m_05;


                break;
        }
    }

    @Override
    protected void StartGame() {
        if (mBallView!=null) {
            mBallView.start();
        }
    }

    void Init() {
        mHandler = new Handler();

        getActivity().findViewById(R.id.container).setBackgroundResource(background);
        //setupStart();


        ViewTreeObserver observer = container.getViewTreeObserver();

        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    setupBallView();

                    ViewTreeObserver obs = container.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }



    private void setupBallView() {
        mBallView = new BallView(getActivity());
        mBallView.setImageResource(ballview);
        int BallWidth = 100;
        int BallHeight = 100;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BallWidth, BallHeight);
        params.leftMargin = container.getWidth() / 2 - BallWidth / 2;
        params.topMargin = container.getHeight() / 2 - BallHeight / 2;
        container.addView(mBallView, params);

        StartFrame.bringToFront();

        AnimationDrawable frameAnimation = (AnimationDrawable) mBallView.getDrawable();
        frameAnimation.setCallback(mBallView);
        frameAnimation.setVisible(true, true);

        frameAnimation.start();


        mBallView.setListener(new BallView.OnObjectTouchEvent() {
            @Override
            public void OnObjectTouchEvent(BookView mView) {
                score = mView.getBookId();
                mBallView.stop();
                //GameOver(score);

                GameOver(OverType.Vote,score);

            }

            @Override
            public void OnObjectOutsideEvent(BookView mView) {

            }
        });

        setupBookLayout();
        /*
        mBallView.addViewList(book1);
        mBallView.addViewList(book2);
        mBallView.addViewList(book3);
        mBallView.addViewList(book4);
        mBallView.addViewList(book5);

        mBallView.setListener(new BallView.OnObjectTouchEvent() {
            @Override
            public void OnObjectTouchEvent(View mView) {
                int index = Integer.valueOf((String)mView.getTag());

                if (answer[index-1]){
                    ((ImageView)mView).setImageResource(bookdrawables[index - 1][1]);
                    userAnswer[index-1] = true;
                    if (checkAnswer()){
                        playVictory();
                    }
                }else{
                    ((ImageView)mView).setImageResource(bookdrawables[index - 1][2]);

                    if (!resetAnswering){
                        resetAnswering = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetAnswering = false;
                                resetAnswer();
                            }
                        },1000);
                    }
                }
            }

            @Override
            public void OnObjectOutsideEvent(View mView) {
            }
        });
        */

    }

    private void OnGameOver(final int score) {
        GameOver(score);

        /*
        playVictory();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GameOver(score);
            }
        },2000);
        */
    }


    BaseGame.OnGameOverListener mListener;

    public void setListener(BaseGame.OnGameOverListener mListener) {
        this.mListener = mListener;
    }

    protected void GameOver(int score) {

        mBallView.stop();
        if (mListener!=null)
            mListener.OnGameOverEvent(BaseGame.OverType.Vote,score);
    }

    private void setupBookLayout() {
        List<BookView> mBooks = new ArrayList<BookView>();

        int allCounter = this.mAnswer.length;

        for (int i = 0; i < allCounter; i++) {
            BookView book = new BookView(getActivity());
            book.setBookId(i);
            Answer answer = mAnswer[i];

            if (mQuestion[0].IsAnswer(answer)){
                book.setQuestionAnswer(true);
            }

            book.setImageResource(answer.getDrawable());

            mBooks.add(book);

        }

        Collections.shuffle(mBooks);

        BookLayout.setArc(45,405);
        for (BookView mBook:mBooks){
            BookLayout.addView(mBook);
        }

        centerIcon.setVisibility(View.GONE);
        centerIcon.setImageResource(mQuestion[0].getDrawable());

        mBallView.setBookViewList(mBooks);
    }

    private void setupStart() {
        Reciprocal.setImageResource(R.anim.reciprocal);
        AnimationDrawable frameAnimation = (AnimationDrawable) Reciprocal.getDrawable();

        if (frameAnimation != null) {
            frameAnimation.setCallback(Reciprocal);
            frameAnimation.setVisible(true, true);

            frameAnimation.start();

            int iDuration = 0;

            for (int i = 0; i < frameAnimation.getNumberOfFrames(); i++) {
                iDuration += frameAnimation.getDuration(i);
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Reciprocal.setVisibility(View.GONE);
                    Start.startAnimation(push_right_to_left);
                }
            }, iDuration);
        }

        push_right_to_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBallView.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private boolean resetAnswering = false;

    private void resetAnswer() {
        userAnswer = new boolean[5];

        /*
        List<ImageView> imageViewList = new ArrayList<ImageView>();
        imageViewList.add(book1);
        imageViewList.add(book2);
        imageViewList.add(book3);
        imageViewList.add(book4);
        imageViewList.add(book5);

        int counter = 0;
        for (ImageView view:imageViewList){
            view.setImageResource(bookdrawables[counter][0]);
            counter++;
        }
        */
    }

    private void playVictory() {
        View Victory = getActivity().findViewById(R.id.Victory);
        if (Victory != null)
            Victory.setVisibility(View.VISIBLE);
        mBallView.stop();
    }

    private boolean checkAnswer() {

        return Arrays.equals(userAnswer, answer);
    }

    @Click
    void reset() {
        mBallView.stop();
        if (getActivity() != null) {
            getActivity().findViewById(R.id.Victory).setVisibility(View.GONE);
        }
        Reciprocal.setVisibility(View.VISIBLE);
        setupStart();
        resetAnswer();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBallView.getLayoutParams();
        if (params != null) {
            params.leftMargin = container.getWidth() / 2 - mBallView.getWidth() / 2;
            params.topMargin = container.getHeight() / 2 - mBallView.getHeight() / 2;
            mBallView.setLayoutParams(params);
        }
    }

}

