package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        //프록시 팩토리는 인스턴스에 인터페이스가 있으면 JDK 동적 프록시를 기본으로 사용하고
        //인터페이스가 없고 구체 클래스만 있다면 CGLIB를 통해서 동적 프록시를 생성함

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClas={}", target.getClass());

        proxy.save();

        //AopUtils.is~~ : ProxyFactory로 만들었을 때만 쓸 수 있음
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();//ProxyFactory로 만들었을 때만 쓸 수 있음
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }
}
