package com.myc.controller;

import com.google.common.collect.Lists;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;

import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/8 11:58
 */
public class GraphqlTypeUnion {
    public static GraphQLObjectType initUserType(){
        List<GraphQLFieldDefinition> fieldDefinitions = Lists.newArrayList();
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLBigInteger).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("sex").type(Scalars.GraphQLString).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLString).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("balance").type(Scalars.GraphQLString).build());
        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                .name("user")
                .fields(fieldDefinitions)
                .build();
        return queryType;
    }

    public static GraphQLObjectType initInfoType(){
        List<GraphQLFieldDefinition> fieldDefinitions = Lists.newArrayList();
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLBigInteger).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("img").type(Scalars.GraphQLString).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("type").type(Scalars.GraphQLString).build());
        fieldDefinitions.add(GraphQLFieldDefinition.newFieldDefinition().name("url").type(Scalars.GraphQLString).build());
        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                .name("info")
                .fields(fieldDefinitions)
                .build();
        return queryType;
    }
}
