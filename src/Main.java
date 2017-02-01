import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        decode(open_file(args[0]));
    }

    public static RandomAccessFile open_file(String file_name) throws FileNotFoundException {
        File fd = new File(file_name);
        return new RandomAccessFile(fd, "r");
    }

    public static void decode(RandomAccessFile f) throws IOException {
        List<Byte> bl = new ArrayList<>();
        Byte b = 0;
        bl.add(b);
        int index = 0;
        Deque<Long> o_index = new ArrayDeque<>();
        int num_of_openers = 0;
        boolean ignore = false;
        int ch_int;
        while ((ch_int = f.read()) != -1) {
            char ch = (char) ch_int;
            //System.out.format("%s:", ch);
            if (ignore && (ch != ']' || ch != '[')) {
                continue;
            }
            switch (ch) {
                case '>':
                    index++;
                    if (index >= bl.size()) {
                        b = 0;
                        bl.add(b);
                    }
                    break;
                case '<':
                    if (index != 0)
                        index--;
                    break;
                case '+':
                    b = bl.get(index);
                    b++;
                    bl.set(index, b);
                    break;
                case '-':
                    b = bl.get(index);
                    if (b == 0) {
                        break;
                    }
                    b--;
                    bl.set(index, b);
                    break;
                case '.':
                    System.out.format("%s",(char) (bl.get(index) & 0xff));
                    break;
                case '[':
                    ignore = bl.get(index) == 0;
                    num_of_openers = o_index.size();
                    o_index.push(f.getFilePointer());
                    break;
                case ']':
                    if (ignore) {
                        if (num_of_openers == o_index.size())
                            ignore = false;
                        else
                            o_index.pop();
                    }
                    else {
                        if (bl.get(index) != 0) {
                            f.seek(o_index.peek());
                        }
                        else
                            o_index.pop();
                    }
                    break;
                default:
                    return;
            }
        }
    }
}