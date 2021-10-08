package qrng.QrngService.RandomNumbers;

import java.util.List;

public interface RandomNumberClientInterface {
    
    List<Byte> requestRandomBytes(String url, int n);

}
