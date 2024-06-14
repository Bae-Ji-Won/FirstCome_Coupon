package com.example.couponcore.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// Redis Lock 설정 파일
@RequiredArgsConstructor
@Component
@Slf4j
public class DistributeLockExecutor {

    private final RedissonClient redissonClient;

        /*
            lockName: 획득하려는 락의 이름입니다.
            waitMilliSecond: 락을 획득하기 위해 대기할 최대 시간(밀리초)입니다.
            leaseMilliSecond: 락을 획득한 후 보유할 최대 시간(밀리초)입니다.
            logic: 락을 획득한 후 실행할 로직을 나타내는 Runnable 객체입니다.
         */
    public void execute(String lockName, long waitMilliSecond, long leaseMilliSecond, Runnable logic) {
        //  지정된 이름의 락을 획득합니다.
        RLock lock = redissonClient.getLock(lockName);
        try{
            // 해당 락에 시간 설정 추가
            boolean isLocked = lock.tryLock(waitMilliSecond, leaseMilliSecond, TimeUnit.MICROSECONDS);
            // Lock 획득 실패시
            if (!isLocked){
                throw new IllegalStateException("["+lockName + "] Lock 획득 실패");
            }
            // Lock 획득 성공시 넘어온 logic 실행
            logic.run();
        }catch (InterruptedException e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            // 현재 스레드가 락을 소유하고 있는 경우 Lock을 풀어줌
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
