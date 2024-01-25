import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.wish.bunny.BuildConfig.AWS_ACCESS_KEY
import com.wish.bunny.BuildConfig.AWS_SECRET_KEY

/**
    작성자: 엄상은
    처리 내용: AWS S3 연결을 위한 객체
*/
class AWSS3Manager {
    companion object {
        private var s3Client: AmazonS3Client? = null

        fun getInstance(): AmazonS3Client {
            if (s3Client == null) {
                val credentials = BasicAWSCredentials(
                    "${AWS_ACCESS_KEY}",
                    "${AWS_SECRET_KEY}"
                )
                s3Client = AmazonS3Client(credentials)
            }
            return s3Client!!
        }
    }
}
