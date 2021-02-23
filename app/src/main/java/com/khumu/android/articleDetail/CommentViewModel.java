import com.khumu.android.data.SimpleComment;

import org.w3c.dom.Comment;

import javax.inject.Inject;

import retrofit2.Call;


public class CommentViewModel extends ViewModel {

    private final static String TAG = "CommentViewModel";

    @Inject
    public CommentService commentService;
    private CommentRepository commentRepository;
    private MutableLiveData<ArrayList<Comment>> comments;
    //article은 변하는 값을 observe할 데이터가 아니라 MutableLiveData로 하지 않아도 된다.
    private Article article;
    private String articleID;
    public CommentViewModel(CommentRepository commentRepository, Article article, String articleID) {
        KhumuApplication.container.inject(this);
        comments = new MutableLiveData<>();
        comments.setValue(new ArrayList<Comment>());
        this.commentRepository = commentRepository;
        this.articleID = articleID;

        this.article = article;
        ListComment();
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataComments(){
        return comments;
    }

    public void ListComment() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ArrayList<Comment> originalComments = comments.getValue();
                    System.out.println(comments);
                    Call<CommentListResponse> call = commentService.getComments(Integer.valueOf(articleID));
                    Log.d(TAG, call.toString());
//                    for (Comment newComment : commentRepository.ListComment(articleID)) {
//                        // 기존에 없던 새로운 comment인지 확인
//                        List<Comment> duplicatedComments = originalComments.stream().filter(item->{
//                            return (newComment.getId() == item.getId());
//                        }).collect(Collectors.toList());
//                        if(duplicatedComments.size() == 0) {
//                            originalComments.add(newComment);
//                        }
//                        else{
//                        }
//                    }
//                    comments.postValue(originalComments);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void CreateComment(SimpleComment comment) throws Exception{
        Call<Comment> call = commentService.createComment("application/json", comment);

//        OkHttpClient client = new OkHttpClient();
//        ObjectMapper mapper = new ObjectMapper();
//        String commentString = mapper.writeValueAsString(comment);
//        JSONObject commentJSON = new JSONObject(commentString);
//        RequestBody authBody = RequestBody.create(MediaType.parse("application/json"),
//                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", Util.DEFAULT_USERNAME, Util.DEFAULT_PASSWORD)
//        );
//        Request authReq = new Request.Builder()
//                .post(authBody)
//                .url(Util.APIRootEndpoint + "token")
//                .build();
//        Response authResp = client.newCall(authReq).execute();
//        String authRespStr = authResp.body().string();
//        String token = new JSONObject(authRespStr).getString("access");
//
//        Request createReq = new Request.Builder()
//                .header("Authorization", "Bearer "+token)
//                .post(RequestBody.create(MediaType.parse("application/json"), commentString))
//                // 임시
//                .url(Util.APIRootEndpoint + "comments" + "?articles=1")
//                .build();
//        Response createResp = client.newCall(createReq).execute();
//        String createRespStr = createResp.body().string();
//        System.out.println("createRespStr: " + createRespStr);
    }
}