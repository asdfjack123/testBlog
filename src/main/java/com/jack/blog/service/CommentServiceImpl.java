package com.jack.blog.service;

import com.jack.blog.dao.CommentRepository;
import com.jack.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        }

        else{
            comment.setParentComment(null);
        }

        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * 循環每個第一層的節點，然後整理底下的節點到第二層
     * @param comments  第一層的節點
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        //copy整個comments到commentsView 因為怕直接操作comments會動到數據庫
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合併2,3,4..層的節點到第2層
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * 循環合併2,3,4..層的節點到第2層
     * @param comments 第一層的節點
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //跌代，存放在tempReplys中
                recursively(reply1);
            }
            //第2層放到對應的第1層
            comment.setReplyComments(tempReplys);
            //清除臨時存放區
            tempReplys = new ArrayList<>();
        }
    }

    //暫存區
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 合併2,3,4..層的節點到第2層
     * @param comment 234...層
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//加到臨時區
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }

}
