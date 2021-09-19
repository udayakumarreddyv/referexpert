package com.referexpert.referexpert.configuration;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.service.EmailSenderService;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.service.impl.RefreshTokenService;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "30s")
@EnableScheduling
@EnableAsync
public class ShedLockConfig {
	public static final Logger logger = LoggerFactory.getLogger(ShedLockConfig.class);

	@Autowired
	private MySQLService mySQLService;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Bean
	public LockProvider lockProvider(@Qualifier("mysqlDataSource") DataSource dataSource) {
		return new JdbcTemplateLockProvider(dataSource, "shedlock");
	}

	@Async
	@Scheduled(cron = "0 1 9 * * *")
	@SchedulerLock(name = "sendEmailToNRUsers", lockAtLeastFor = "10s", lockAtMostFor = "50s")
	public void sendEmailToNRUsers() {
		logger.info("In sendEmailToNRUsers");
		List<UserReferral> userReferrals = mySQLService.selectNonRegisteredUsers();
		if (userReferrals != null) {
			userReferrals.stream()
					.forEach((c) -> emailSenderService.sendReferralEMail(c.getDocEmail(), c.getUserReferralId()));
		}
	}

	@Async
	@Scheduled(cron = "0 */30 * * * *")
	@SchedulerLock(name = "removeExpiredTokens", lockAtLeastFor = "10s", lockAtMostFor = "50s")
	public void removeExpiredTokens() {
		logger.info("In removeExpiredTokens");
		refreshTokenService.cleanupRefreshToken();
		logger.info("Expired tokens removed successfully");
	}
}