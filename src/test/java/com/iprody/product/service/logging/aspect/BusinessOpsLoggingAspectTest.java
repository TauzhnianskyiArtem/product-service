package com.iprody.product.service.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.logcapture.junit5.LogCaptureExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;
import static org.logcapture.assertion.ExpectedLoggingMessage.aLog;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessOpsLoggingAspectTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private BusinessOpsLogging businessOpsLogging;

    @InjectMocks
    private BusinessOpsLoggingAspect businessOpsLoggingAspect;

    @Test
    void shouldAroundCallForAddLoggingWithSuccessCompletion() throws Throwable {

        final String initiatingLogMessage = "Initiating business operation on the TestDomain: "
                                                                + "testMethod, "
                                                                + "with input Arguments: [arg1, arg2].";
        final String completionLogMessage = "Completion of business operation on the TestDomain: "
                                                                + "testMethod, "
                                                                + "Resulting value: testResult.";
        final Signature signatureMock = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(signatureMock);
        when(signatureMock.getName()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});
        when(businessOpsLogging.domain()).thenReturn("TestDomain");
        when(businessOpsLogging.level()).thenReturn(Level.INFO);
        when(joinPoint.proceed()).thenReturn("testResult");

        businessOpsLoggingAspect.aroundCallForAddLogging(joinPoint, businessOpsLogging);


        logCaptureExtension.logged(aLog().info().withMessage(equalTo(initiatingLogMessage)));
        logCaptureExtension.logged(aLog().info().withMessage(equalTo(completionLogMessage)));
    }

    @Test
    void shouldAroundCallForAddLoggingWithThrowException() throws Throwable {

        final String initiatingLogMessage = "Initiating business operation on the TestDomain: "
                                                                + "testMethod, "
                                                                + "with input Arguments: [arg1, arg2].";
        final String throwExceptionLogMessage = "Exception encountered during business operation on the TestDomain: "
                                                                + "testMethod,"
                                                                + " Message: Test Message";
        final Signature signatureMock = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(signatureMock);
        when(signatureMock.getName()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});
        when(businessOpsLogging.domain()).thenReturn("TestDomain");
        when(businessOpsLogging.level()).thenReturn(Level.INFO);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test Message"));

        assertThatThrownBy(() ->
                businessOpsLoggingAspect.aroundCallForAddLogging(joinPoint, businessOpsLogging))
                .isInstanceOf(RuntimeException.class);


        logCaptureExtension.logged(aLog().info().withMessage(equalTo(initiatingLogMessage)));
        logCaptureExtension.logged(aLog().info().withMessage(equalTo(throwExceptionLogMessage)));
    }


}
