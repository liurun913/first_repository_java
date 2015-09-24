/*
*JAVAʵ�ִ������ļ��в��ҵ��ʳ���Ƶ�ʵĹ���
*
*���ȿ��ǵ��Ǵ��ļ��򿪷�ʽ�����⣬ֱ����IO��������ʱ��Ƚϳ����鵽������NIO��file MAP���ڴ棬Ч�ʱȽϸߡ�(����ǰ�����ڴ��㹻��)
*Ȼ���ǶԵ��ʼ��������⡣��ĳ������ͳ�ƣ���Java���ҵĽӿڣ����β�ѯ�Աȣ��õ�����ֵ�������õ���pattern�ࡣ
*Ҳ���������������Hash�����еĵ��ʳ��ֵĴ���ͳ�Ƴ������ó���Ҫ�ĵ��ʳ���Ƶ�ʣ��о�����һ�����ʲ���û��Ҫ��û�������ˡ�
*/
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CountWordInLargeFiles
{
    static long length = 2^32-1;//max int length
    public static void main(String[] args) throw Exception
    {
        RandomAccessFile file = new RandomAccessFile("D:\\largefile.txt","r");
        FileChannel fc = file.getChannel();
	long fc_counter = fc.size()/length;//split the file to several buffer
	long i = 0;
	String goal = "test";
	long count = 0;//the time of goal word found in the file
	while (fc_counter>0)
	{
	    count += countfrombuffer(fc,i,length,goal);//map max size one time
	    i += length;
	    fc_counter--;
	}
	if (i<fc.size())
	{
	    count += countfrombuffer(fc,i,(fc.size()-i),goal);//map max left data
	}
	System.out.println("find the word"+goal+": "+count);
	return;
    }
    private long countfrombuffer(FileChannel fchnl, long offset, long len, String str) throws Exception
    {
	long cnt = 0;
	ByteBuffer bb =null;
	bb = fchnl.map(fchnl.MapMode.READ_ONLY,offset,len);
	//find the goal word in the buffer
	Pattern pattern = Pattern.compile(str);
	String content = bb.toString();
	Matcher matcher = pattern.matcher(content);
	while (matcher.find())
	{
	    cnt++;
	}
	bb.clear();
	return cnt;
    }
}