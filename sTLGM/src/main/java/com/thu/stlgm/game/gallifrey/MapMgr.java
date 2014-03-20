package com.thu.stlgm.game.gallifrey;


import com.thu.stlgm.game.gallifrey.bean.ChessBoard;
import com.thu.stlgm.game.gallifrey.bean.LeftistBomb;
import com.thu.stlgm.game.gallifrey.bean.RightistBomb;
import com.thu.stlgm.game.gallifrey.bean.Star;
import com.thu.stlgm.game.gallifrey.bean.StraightLineBomb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by SemonCat on 2014/3/19.
 */
public class MapMgr {

    public static ChessBoard BaseMapGenerator(){
        ChessBoard mChessBoard = new ChessBoard(7,3);

        StraightLineBomb mStraightLineBomb = new StraightLineBomb(4, 1);
        mStraightLineBomb.setFixed(true);

        mChessBoard.setChess(new Star(0, 2));
        mChessBoard.setChess(new Star(2, 2));
        Star mTrue01 = new Star(3,2);
        mTrue01.setAnswer(true);
        mChessBoard.setChess(mTrue01);
        mChessBoard.setChess(new Star(5, 0));
        mChessBoard.setChess(new Star(5, 2));
        Star mTrue02 = new Star(6,0);
        mTrue02.setAnswer(true);
        mChessBoard.setChess(mTrue02);
        Star mTrue03 = new Star(6,2);
        mTrue03.setAnswer(true);
        mChessBoard.setChess(mTrue03);

        mChessBoard.setChess(mStraightLineBomb);

        mChessBoard.setTargetBomb(mStraightLineBomb);

        return mChessBoard;
    }

    public static ChessBoard MapGenerator(int type){
        ChessBoard mChessBoard = new ChessBoard(7,3);
        switch (type) {
            case 0:
                StraightLineBomb mStraightLineBomb = new StraightLineBomb(4, 1);
                mStraightLineBomb.setFixed(true);

                mChessBoard.setChess(new Star(0, 2));
                mChessBoard.setChess(new Star(2, 2));
                Star mTrue01 = new Star(3, 2);
                mTrue01.setAnswer(true);
                mChessBoard.setChess(mTrue01);
                mChessBoard.setChess(new Star(5, 0));
                mChessBoard.setChess(new Star(5, 2));
                Star mTrue02 = new Star(6, 0);
                mTrue02.setAnswer(true);
                mChessBoard.setChess(mTrue02);
                Star mTrue03 = new Star(6, 2);
                mTrue03.setAnswer(true);
                mChessBoard.setChess(mTrue03);

                mChessBoard.setChess(mStraightLineBomb);

                mChessBoard.setTargetBomb(mStraightLineBomb);


                break;
            case 1:
                RightistBomb mRightistBomb = new RightistBomb(3, 2);
                mRightistBomb.setFixed(true);

                mChessBoard.setChess(new Star(0, 0));
                mChessBoard.setChess(new Star(1, 2));
                Star mTrue04 = new Star(2,0);
                mTrue04.setAnswer(true);
                mChessBoard.setChess(mTrue04);
                mChessBoard.setChess(new Star(2, 1));
                mChessBoard.setChess(new Star(5, 0));
                Star mTrue05 = new Star(6, 0);
                mTrue05.setAnswer(true);
                mChessBoard.setChess(mTrue05);
                Star mTrue06 = new Star(6, 2);
                mTrue06.setAnswer(true);
                mChessBoard.setChess(mTrue06);

                mChessBoard.setChess(mRightistBomb);

                mChessBoard.setTargetBomb(mRightistBomb);

                break;
            case 2:
                LeftistBomb mLeftistBomb01 = new LeftistBomb(6, 1);
                mLeftistBomb01.setFixed(true);

                Star mTrue07 = new Star(0,0);
                mTrue07.setAnswer(true);
                mChessBoard.setChess(mTrue07);
                Star mTrue08 = new Star(2, 1);
                mTrue08.setAnswer(true);
                mChessBoard.setChess(mTrue08);
                Star mTrue09 = new Star(5, 2);
                mTrue09.setAnswer(true);
                mChessBoard.setChess(mTrue09);


                mChessBoard.setChess(new Star(0, 1));
                mChessBoard.setChess(new Star(1, 2));
                mChessBoard.setChess(new Star(3, 0));
                mChessBoard.setChess(new Star(5, 1));

                mChessBoard.setChess(mLeftistBomb01);

                mChessBoard.setTargetBomb(mLeftistBomb01);


                break;
            case 3:

                StraightLineBomb mStraightLineBomb02 = new StraightLineBomb(0, 0);
                mStraightLineBomb02.setFixed(true);

                Star mTrue10 = new Star(1,0);
                mTrue10.setAnswer(true);
                mChessBoard.setChess(mTrue10);
                Star mTrue11 = new Star(1, 2);
                mTrue11.setAnswer(true);
                mChessBoard.setChess(mTrue11);
                Star mTrue12 = new Star(2, 0);
                mTrue12.setAnswer(true);
                mChessBoard.setChess(mTrue12);


                mChessBoard.setChess(new Star(2, 2));
                mChessBoard.setChess(new Star(3, 0));
                mChessBoard.setChess(new Star(3, 2));
                mChessBoard.setChess(new Star(5, 1));

                mChessBoard.setChess(mStraightLineBomb02);

                mChessBoard.setTargetBomb(mStraightLineBomb02);

                break;
            case 4:

                LeftistBomb mLeftistBomb02 = new LeftistBomb(2, 1);
                mLeftistBomb02.setFixed(true);

                Star mTrue13 = new Star(2,2);
                mTrue13.setAnswer(true);
                mChessBoard.setChess(mTrue13);
                Star mTrue14 = new Star(4, 0);
                mTrue14.setAnswer(true);
                mChessBoard.setChess(mTrue14);
                Star mTrue15 = new Star(5, 0);
                mTrue15.setAnswer(true);
                mChessBoard.setChess(mTrue15);


                mChessBoard.setChess(new Star(0, 2));
                mChessBoard.setChess(new Star(2, 0));
                mChessBoard.setChess(new Star(3, 2));
                mChessBoard.setChess(new Star(5, 1));

                mChessBoard.setChess(mLeftistBomb02);

                mChessBoard.setTargetBomb(mLeftistBomb02);

                break;

        }

        return mChessBoard;
    }

    public static List<ChessBoard> Map01Generator(){
        List<ChessBoard> mChessBoardList = new ArrayList<ChessBoard>();

        for (int i=0;i<5;i++){
            ChessBoard mChessBoard = new ChessBoard(7,3);

            switch (i){
                case 0:
                    StraightLineBomb mStraightLineBomb = new StraightLineBomb(4, 1);
                    mStraightLineBomb.setFixed(true);

                    mChessBoard.setChess(new Star(0, 2));
                    mChessBoard.setChess(new Star(2, 2));
                    Star mTrue01 = new Star(3,2);
                    mTrue01.setAnswer(true);
                    mChessBoard.setChess(mTrue01);
                    mChessBoard.setChess(new Star(5, 0));
                    mChessBoard.setChess(new Star(5, 2));
                    Star mTrue02 = new Star(6,0);
                    mTrue02.setAnswer(true);
                    mChessBoard.setChess(mTrue02);
                    Star mTrue03 = new Star(6,2);
                    mTrue03.setAnswer(true);
                    mChessBoard.setChess(mTrue03);

                    mChessBoard.setChess(mStraightLineBomb);

                    mChessBoard.setTargetBomb(mStraightLineBomb);

                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;

            }


            mChessBoardList.add(mChessBoard);
        }

        return mChessBoardList;
    }
}
