package com.javix.wordflipster


class ChallengeRepository(private val challengeDao: ChallengeDao) {
    suspend fun addChallenge(challenge: ChallengeEntity) {
        challengeDao.insertChallenge(challenge)
    }

    suspend fun getChallenges(): List<ChallengeEntity> {
        return challengeDao.getAllChallenges()
    }
}
