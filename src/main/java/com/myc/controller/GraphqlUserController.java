package com.myc.controller;

import com.myc.comm.ApiResultModel;
import com.myc.entity.User;
import com.myc.service.UserService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLSchema;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.math.BigInteger;
import java.util.Map;

import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/7 14:15
 */
@RestController
@RequestMapping("graph")
public class GraphqlUserController {
    @Resource
    UserService userService;

    @ApiOperation(value = "Graphql方式获取所有的用户信息", notes = "参数描述", code = 200, produces = "application/json")
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ApiResultModel<User> getUserList(@RequestParam(value = "query", required = false) String query){
        String testquery = "{user(id:4,name:\"11\"){id,name,sex,age}}";
        if(!StringUtils.isEmpty(query)){
            testquery = query;
        }

        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(newObject().name("selectUser").field(GraphQLFieldDefinition.newFieldDefinition().name("user")
                        .argument(newArgument().name("id").type(Scalars.GraphQLInt).build())
                        .argument(newArgument().name("name").type(Scalars.GraphQLString).build())
                        .type(GraphqlTypeUnion.initUserType())
                        .dataFetcher(dataFetchingEnvironment -> {
                            int id = dataFetchingEnvironment.getArgument("id");
                            String name = dataFetchingEnvironment.getArgument("name");
                            User user = new User();
                            user.setId(id);
                            user.setUserName(name);
                            return userService.queryOne(user);
                        }).build()))
                .build();

        Map<String, Object> result = GraphQL.newGraphQL(schema).build().execute(testquery).getData();


        return ApiResultModel.SUCCESS(result);
    }
}
