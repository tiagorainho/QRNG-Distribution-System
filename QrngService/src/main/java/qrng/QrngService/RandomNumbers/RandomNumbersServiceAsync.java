package qrng.QrngService.RandomNumbers;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import qrng.QrngService.Generator.Generator;

@Service
public class RandomNumbersServiceAsync {

    @Autowired
    private final RandomNumbersDao randomNumbersDao;

    @Autowired
    private final RandomNumberClient randomNumberClient;

    public RandomNumbersServiceAsync(@Qualifier("in_memory") RandomNumbersDao randomNumbersDao, RandomNumberClient randomNumberClient) {
        this.randomNumbersDao = randomNumbersDao;
        this.randomNumberClient = randomNumberClient;
    }

    @Async
    public void refillRandomNumbers(Generator generator, int bytesToRetrieve) {
        List<Byte> bytes = requestRandomBytes(generator.getUrl(), bytesToRetrieve);
        randomNumbersDao.add(generator.getName(), bytes);
    }

    public List<Byte> requestRandomBytes(String url , int bytesToRetrieve) {
        // receive the number of bytes per element from the array of random bits
        return randomNumberClient.requestRandomBytes(url, bytesToRetrieve);

        // create list from those bits

        
        //return Stream.generate(() -> Byte.valueOf((byte)1)).limit(bytesToRetrieve).toList();
    }
    
}
