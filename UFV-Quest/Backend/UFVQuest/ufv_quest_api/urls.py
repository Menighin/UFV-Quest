from django.conf.urls import patterns, url

from ufv_quest_api import views

urlpatterns = patterns('',
	url(r'^$', views.index, name='index'),

	url(r'^user/(?P<user_id>\d+)/$', views.user, name='user'),
)
