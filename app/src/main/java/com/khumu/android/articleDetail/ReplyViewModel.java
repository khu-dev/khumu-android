package com.khumu.android.articleDetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khumu.android.data.Comment;
import com.khumu.android.repository.ReplyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyViewModel extends ViewModel {
    private final static String TAG = "ReplyViewModel";
    private ReplyRepository replyRepository;
    private MutableLiveData<ArrayList<Comment>> replies;
    private String commentID;
    public ReplyViewModel(ReplyRepository replyRepository, String commentID) {
        replies = new MutableLiveData<>();
        replies.setValue(new ArrayList<Comment>());
        this.replyRepository = replyRepository;
        this.commentID = commentID;
        ListReply();
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataReplies() { return replies; }

    private void ListReply() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ArrayList<Comment> originalReplies = replies.getValue();
                    for (Comment newReply : replyRepository.ListReply(commentID)) {
                        // 기존에 없던 새로운 comment인지 확인
                        List<Comment> duplicatedReplies = originalReplies.stream().filter(item->{
                            return (newReply.getID() == item.getID());
                        }).collect(Collectors.toList());
                        if(duplicatedReplies.size() == 0) {
                            originalReplies.add(newReply);
                        }
                        else{
                        }
                    }
                    replies.postValue(originalReplies);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
