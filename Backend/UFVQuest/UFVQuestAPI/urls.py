from django.conf.urls import patterns, url

from UFVQuestAPI import views

urlpatterns = patterns('',
	url(r'^$', views.index, name='index'),

	url(r'^user/(?P<user_id>\d+)/$', views.user, name='user'),
	url(r'^createUser$', views.createUser, name='createUser'),
	url(r'^createQuestGoToAndAnswer', views.createQuestGoToAndAnswer, name='createQuestGoToAndAnswer'),
	url(r'^createQuestSeekAndAnswer', views.createQuestSeekAndAnswer, name='createQuestSeekAndAnswer'),
	url(r'^getQuestTypes', views.getQuestTypes, name='getQuestTypes'),
	url(r'^getActiveQuests', views.getActiveQuests, name='getActiveQuests'),
	url(r'^getUserRanking', views.getUserRanking, name='getUserRanking'),
	url(r'^getQuestHistory', views.getQuestHistory, name='getQuestHistory'),
	url(r'^getQuestIStillCanComplete', views.getQuestIStillCanComplete, name='getQuestIStillCanComplete'),
	url(r'^tryQuest', views.tryQuest, name='tryQuest'),
	url(r'^spendEnergy', views.spendEnergy, name='spendEnergy'),
)
