# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Quest',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('title', models.CharField(max_length=120)),
                ('description', models.CharField(max_length=200)),
                ('place_name', models.CharField(max_length=100, null=True)),
                ('latitude', models.FloatField(default=0.0)),
                ('longitude', models.FloatField(default=0.0)),
                ('points', models.IntegerField(default=0)),
                ('created_on', models.DateTimeField(auto_now_add=True, verbose_name=b'created on')),
                ('expiration_date', models.DateTimeField(null=True)),
                ('diary', models.BooleanField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='QuestType',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50)),
                ('description', models.CharField(max_length=280)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='GoToAndAnswer',
            fields=[
                ('questtype_ptr', models.OneToOneField(parent_link=True, auto_created=True, primary_key=True, serialize=False, to='ufv_quest_api.QuestType')),
                ('question', models.CharField(max_length=180)),
                ('answer1', models.CharField(max_length=100)),
                ('answer2', models.CharField(max_length=100)),
                ('answer3', models.CharField(max_length=100)),
                ('answer4', models.CharField(max_length=100)),
                ('correct_answer', models.IntegerField(default=0)),
            ],
            options={
            },
            bases=('ufv_quest_api.questtype',),
        ),
        migrations.CreateModel(
            name='SeekAndAnswer',
            fields=[
                ('questtype_ptr', models.OneToOneField(parent_link=True, auto_created=True, primary_key=True, serialize=False, to='ufv_quest_api.QuestType')),
                ('question', models.CharField(max_length=180)),
                ('answer', models.CharField(max_length=300)),
            ],
            options={
            },
            bases=('ufv_quest_api.questtype',),
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('facebook_id', models.CharField(max_length=200)),
                ('energy_left', models.IntegerField(default=0)),
                ('last_seen', models.DateTimeField(verbose_name=b'last seen')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='UserAttemptsQuest',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('solved', models.BooleanField(default=False)),
                ('points_won', models.IntegerField()),
                ('quest', models.ForeignKey(to='ufv_quest_api.Quest')),
                ('user', models.ForeignKey(to='ufv_quest_api.User')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='quest',
            name='created_by',
            field=models.ForeignKey(to='ufv_quest_api.User'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='quest',
            name='quest_type',
            field=models.ForeignKey(to='ufv_quest_api.QuestType'),
            preserve_default=True,
        ),
    ]
