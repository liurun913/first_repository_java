/*
*JAVA实现大容量文件中查找单词出现频率的功能
*
*首先考虑的是大文件打开方式的问题，直接用IO操作花费时间比较长，查到可以用NIO把file MAP到内存，效率比较高。(不过前提是内存足够大)
*然后是对单词计数的问题。对某个单词统计，用Java查找的接口，依次查询对比，得到最后的值。这里用的是pattern类。
*也可以用外排序或者Hash把所有的单词出现的次数统计出来，得出想要的单词出现频率，感觉对于一个单词查找没必要就没这样做了。
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