package com.fysiki.workoutkitsdkdemo

import com.apollographql.apollo.api.ApolloResponse
import com.fysiki.workoutkitsdkdemo.CloudClient.apolloClient

class WorkoutRepository {

    suspend fun getDemoWorkouts(): ApolloResponse<GetSessionsQuery.Data> {
        return apolloClient.query(GetSessionsQuery()).execute()
    }

    suspend fun getDemoWorkout(id: String): ApolloResponse<GetSessionQuery.Data> {
        return apolloClient.query(GetSessionQuery(id)).execute()
    }

    suspend fun getDemoWorkoutContent(id: String): ApolloResponse<GetSessionContentQuery.Data> {
        return apolloClient.query(GetSessionContentQuery(id)).execute()
    }
}