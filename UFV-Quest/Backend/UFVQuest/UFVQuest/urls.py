from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
	url(r'^ufv_quest_api/', include('ufv_quest_api.urls')),
    url(r'^admin/', include(admin.site.urls)),
)
